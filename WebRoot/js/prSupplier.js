/*** 
 *	檔案: prSupplier.js
 *	說明: 資訊廠商維護作業
 */
 
var vnB_Button = 0; 
var vnB_Header = 1;


function outlineBlock(){
	
	formInitial();
 	buttonLine();	
	headerInitial();
	//parseURL();
}

// 搜尋初始化
function formInitial(){ 
	
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
		vat.bean().vatBeanOther = 
		{
			loginBrandCode     : document.forms[0]["#loginBrandCode"    	].value,   	
			loginEmployeeCode  : document.forms[0]["#loginEmployeeCode" 	].value,  	     
			formId           	: document.forms[0]["#formId"            	].value,
			category		      	: "", 
			supplierNo:"",
			currentRecordNumber : 0,
			lastRecordNumber : 0
		};
	    vat.bean.init(	
	  		function(){
				return "process_object_name=prSupplierAction&process_object_method_name=performPrSupplierInitial";
				  	},{								
	    		other: true, bind: true
    	   	}
 		); 	
	}
}

//可搜尋的欄位
//先以手動方式輸入資料
function headerInitial(){
	//var allVipTypeCodes = vat.bean("allVipTypeCodes"); // 折扣卡別下拉選單，在 service.executeInitial() 中取得 DAO 的值  
	//var allItemDiscountTypes = vat.bean("allItemDiscountTypes"); // 商品折扣類型下拉選單，在 service.executeInitial() 中取得 DAO 的值 
	var vaOpt1 = [["", "", ""], ["二聯式", "三聯式"], ["二聯式", "三聯式"]];
	var loginEmployeeCode  = document.forms[0]["#loginEmployeeCode" 	].value;
	vat.block.create(
		vnB_Header,
		{
			id: "vatBlock_Head", generate: true,
			table:"cellspacing='1' class='default' border='0' cellpadding='2'",
			title:"資訊廠商維護作業", 
			rows:[  
				{row_style:"", cols:[
			 			{items:[{name:"#L.supplierNo", type:"LABEL", value:"廠商代碼*"}]},	 
			 			{items:[{name:"#F.supplierNo", type:"TEXT", bind:"supplierNo", size : 5, maxLen : 5}]},		 
			 			{items:[{name:"#L.unifiedUnmbering", type:"LABEL", value:"統一編號*"}]},
						{items:[{name:"#F.unifiedUnmbering", type:"TEXT", bind:"unifiedUnmbering", size:8, maxLen : 8},
			 		 			{name:"#F.headId", type:"TEXT", bind:"headId", mode:"READONLY", size:6},
			 		 			{name:"#F.orderTypeCode", type:"TEXT", bind:"orderTypeCode", mode:"READONLY", size:15},
			 		 			{name:"#F.orderNo", type:"TEXT", bind:"orderNo", mode:"READONLY", size:15}]},
			 		 	{items : [{name : "#F.isAssessSup", type : "CHECKBOX",  bind : "isAssessSup"}, 
			 		 			{name : "#L.isAssessSup", type : "LABEL", value : "isAssessSup?", size : 10}],td : "colSpan=2"}
					]
				},		
				{
					row_style:"",
					cols:[
						{items:[{name:"#L.name", type:"LABEL", value:"簡稱"}]},
						{items:[{name:"#F.name", type:"TEXT", bind:"name" , size : 6, maxLen : 6}]},
						{items:[{name:"#L.supplier", type:"LABEL", value:"廠商名稱*"}]},
						{items:[{name:"#F.supplier", type:"TEXT", bind:"supplier", size : 50, maxLen : 50}], td:"colSpan=3"}
					]
				},
				{
					row_style:"",
					cols:[
						{items:[{name:"#L.tel", type:"LABEL", value:"電話"}]},	 
			 			{items:[{name:"#F.tel", type:"TEXT", bind:"tel", size : 15, maxLen : 15}]},
			 			{items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"發票類型"}]},
			 			{items:[{name:"#F.invoiceTypeCode", type:"SELECT", bind:"invoiceTypeCode", init:vaOpt1}]},  
						{items:[{name:"#L.executeInCharge", type:"LABEL", value:"處理人*"}]},
						{items:[{name:"#F.executeInCharge", type:"TEXT", bind:"executeInCharge"}]}
					]
				},
				{
					row_style:"",
					cols:[
						{items:[{name:"#L.fax", type:"LABEL", value:"傳真", size : 15, maxLen : 15}]},	 
			 			{items:[{name:"#F.fax", type:"TEXT", bind:"fax", size : 15, maxLen : 15}]},
			 			{items:[{name:"#L.invoiceTypeCode", type:"LABEL", value:"*為必填欄位，請務必填寫"}], td:"colSpan=4"}
					]
				}
			], 	 
			beginService:"",
			closeService:""			
		}
	);
}

function buttonLine(){
	var vsMaxRecord = 0;
	var vsCurrentRecord = 0;
    vat.block.create(
    	vnB_Button, 
    	{
			id: "vatBlock_Button", 
			generate: true,
			table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title:"", 
			rows:[  
	 			{
	 				row_style:"", 
	 				cols:[
	 					{
	 						items:[	
	 							{
					 				name : "#B.search", type :"PICKER", value : "查詢", src : "./images/button_find.gif", openMode : "open",
					 				service : "Pr_Supplier:search:20170807.page", left : 0, right : 0, width : 1024, height : 768,	
					 				serviceAfterPick : function(){doAfterPickerProcess();}
					 			},
	 							{name : "SPACE"           , type:"LABEL"  ,value:"　"},
	 							{name : "#B.submit"       , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 	 						{name : "SPACE"           , type:"LABEL"  ,value:"　"},
	 	 						{name : "#B.exit"         , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  , eClick:'closeWindows("CONFIRM")'},
	 	 						{name : "SPACE"	  		  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.first"		  , type:"IMG"	  ,value:"第一筆", src : "./images/play-first.png", eClick : "gotoFirst()"},
					 			{name : "SPACE"			  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.forward"	  , type:"IMG"    ,value:"上一筆", src : "./images/play-back.png", eClick : "gotoForward()"},
					 			{name : "SPACE"   		  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.next"		  , type:"IMG"    ,value:"下一筆",  src : "./images/play.png", eClick : "gotoNext()"},
					 			{name : "SPACE"    		  , type:"LABEL"  ,value:"　"},
					 			{name : "#B.last"		  , type:"IMG"	  ,value:"最後一筆", src : "./images/play-forward.png", eClick : "gotoLast()"},
					 			{name : "#L.currentRecord", type:"NUMB"   ,bind:vsCurrentRecord, size : 4, mode :"READONLY"},
					 			{name : "SPACE"			  , type:"LABEL"  ,value:" / "},
					 			{name : "#L.maxRecord"    , type:"NUMB"   ,bind:vsMaxRecord, size : 4, mode : "READONLY"}
	 	 					],
	 			  			td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
	 			  		}
	 			  ]
	 			 }
	  		], 	 
			beginService:"",
			closeService:""			
		}
	);
}

/* 離開 */
function closeWindows(closeType){
  var isExit = true ;
  if("CONFIRM" == closeType){
	isExit = confirm("是否確認離開?");
  }
  if(isExit){
    	window.top.close();
   }
}


	
function doSubmit(formAction){
	vat.bean().vatBeanOther.formId = vat.item.getValueByName("#F.headId");
	//var formId    = document.forms[0]["#formId"].value.replace(/^\s+|\s+$/, '');
    var status    = vat.item.getValueByName("#F.status");
        
	var alertMessage ="是否確定送出?";
	var formStatus = status;
    if("SAVE" == formAction){
        formStatus = "SAVE";
    }else if("SUBMIT" == formAction){
        //formStatus = changeFormStatus(formId, processId, status, approvalResult);
    }else if("VOID" == formAction){
        formStatus = "VOID";
    }	
    var alertMessage ="是否確定送出?";
	if("SUBMIT" == formAction){
	    alertMessage = "是否確定送出?";
	}
	if(confirm(alertMessage)){
		vat.bean().vatBeanOther.formAction = formAction;
		vat.bean().vatBeanOther.formStatus = formStatus;
		vat.block.submit(function(){
				return "process_object_name=prSupplierAction"+
				"&process_object_method_name=performModTransaction";
			},{
				bind:true, link:true, other:true 				
			}
		);
	}
}

/* 指定下一個狀態 */
function changeFormStatus(formId, processId, status, approvalResult){
    var formStatus = "";
    if(formId == null || formId == "" || status == "UNCONFIRMED"){
        formStatus = "SIGNING";
    }else if(processId != null && processId != "" && processId != 0){
    	if(status == "SAVE" || status == "REJECT"){
			formStatus = "SIGNING";
        }
        if( status == "REJECT"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }else if (status == "SIGNING"){
            if(approvalResult == "true"){
                formStatus = "SIGNING";
            }else{
                formStatus = "REJECT";
            }
        }else{
        	formStatus = status;
        }
    }else if(status == "FINISH"){
    	formStatus = "FINISH";
    }else{
    	formStatus = "SIGNING";
    }
    return formStatus;
}

/* 下拉撈資料 */
function finditemdata(){
		//	alert("111");
	var processString = "process_object_name=imItemDiscountService&process_object_method_name=getAJAXFindDataByItemDiscount" +
						"&loginBrandCode=" + vat.item.getValueByName("#F.brandCode") +
						"&vipTypeCode=" + vat.item.getValueByName("#F.vipTypeCode") + 
						"&itemDiscountCode=" + vat.item.getValueByName("#F.itemDiscountCode");
	vat.ajax.startRequest(processString,  function() { 
	  if (vat.ajax.handleState())
	  	vat.item.setValueByName("#F.discount", vat.ajax.getValue("discount", vat.ajax.xmlHttp.responseText));
	  	vat.item.setValueByName("#F.beginDate", vat.ajax.getValue("beginDate", vat.ajax.xmlHttp.responseText));
	  	vat.item.setValueByName("#F.endDate", vat.ajax.getValue("endDate", vat.ajax.xmlHttp.responseText));
		vat.item.setValueByName("#F.enable", vat.ajax.getValue("enable", vat.ajax.xmlHttp.responseText));
		vat.item.setValueByName("#F.createDate", vat.ajax.getValue("createDate", vat.ajax.xmlHttp.responseText));			
	 });
}

function clearForm(){
			vat.item.setValueByName("#F.vipTypeCode", "");
			vat.item.setValueByName("#F.itemDiscountType", "");
			vat.item.setValueByName("#F.discount", "");
			vat.item.setValueByName("#F.beginDate", "");
			vat.item.setValueByName("#F.endDate", "");
			vat.item.setValueByName("#F.enable", "");
}

// 送出的返回
function createRefreshForm(){
     vat.bean().vatBeanPicker.result = null; 
     vat.bean().vatBeanOther.category = "";
     vat.item.setValueByName("#L.currentRecord", "0");
     vat.item.setValueByName("#L.maxRecord", "0");
	 refreshForm("");
}


// 刷新頁面
function refreshForm(code){
	vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	vat.bean().vatBeanOther.supplierNo = code; 
	vat.block.submit(
		function(){
			return "process_object_name=prSupplierAction&process_object_method_name=performPrSupplierInitial"; 
     	},{other      : true,bind      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				//doFormAccessControl();權限控制
     	}}
    );	
}


// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
	if(vat.bean().vatBeanPicker.result != null){
		var vsMaxSize = vat.bean().vatBeanPicker.result.length;
		if(vsMaxSize === 0){
			vat.bean().vatBeanOther.firstRecordNumber = 0;
			vat.bean().vatBeanOther.lastRecordNumberm= 0;
			vat.bean().vatBeanOther.currentRecordNumber = 0;
		}
		else{
			vat.bean().vatBeanOther.firstRecordNumber = 1;
			vat.bean().vatBeanOther.lastRecordNumber = vsMaxSize ;
			vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
			vat.bean().vatBeanOther.category       = "master";
			var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber - 1].supplierNo;
			//alert(code);
			refreshForm(code);
		}
		vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
		vat.item.setValueByName("#L.maxRecord", vat.bean().vatBeanOther.lastRecordNumber);
	}
}

function gotoFirst(){
	if(vat.bean().vatBeanOther.firstRecordNumber >0) {
	  if(vat.bean().vatBeanOther.currentRecordNumber - 1 >= vat.bean().vatBeanOther.firstRecordNumber){
	    vat.bean().vatBeanOther.currentRecordNumber = vat.bean().vatBeanOther.firstRecordNumber;
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].supplierNo;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    vat.bean().vatBeanOther.category       = "master";
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].supplierNo;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	vat.bean().vatBeanOther.category       = "master";
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].supplierNo;
	  	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	  	vat.bean().vatBeanOther.category       = "master";
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].supplierNo;
	    vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
	    vat.bean().vatBeanOther.category       = "master";
	    refreshForm(code);
	  }else{
	  	alert("目前已在最後一筆資料");
	  }
	}else{
		alert("無資料可供翻頁");
	}
}
/*
function parseURL(){

	var strUrl = location.search;  
	var getPara, ParaVal;  
	var aryPara = [];
	var sendBrandCode ;
	var vipTypeCode;
  	var isOppositePicker;
  	var itemDiscountType; 
	if (strUrl.indexOf("?") != -1) {  
		var getSearch = strUrl.split("?");  
    getPara = getSearch[1].split("&");
    for (i = 0; i < getPara.length; i++) {  
      ParaVal = getPara[i].split("=");
      if(ParaVal[0] == "vipTypeCode"){
      		vat.item.setValueByName("#F.vipTypeCode", ParaVal[1]);
      		vat.item.setAttributeByName("#F.vipTypeCode", "readOnly", true);
      }else  if(ParaVal[0] == "itemDiscountType"){
      		vat.item.setValueByName("#F.itemDiscountType", ParaVal[1]);
      		vat.item.setAttributeByName("#F.itemDiscountType", "readOnly", true);
      }else  if(ParaVal[0] == "discount"){
      		vat.item.setValueByName("#F.discount", ParaVal[1]);
      }else  if(ParaVal[0] == "beginDate"){
      		vat.item.setValueByName("#F.beginDate", ParaVal[1]);
      }else  if(ParaVal[0] == "endDate"){
      		vat.item.setValueByName("#F.endDate", ParaVal[1]);
      }else  if(ParaVal[0] == "createdBy"){
      		vat.item.setValueByName("#F.createdBy", ParaVal[1]);
      }else  if(ParaVal[0] == "creationDate"){
      		vat.item.setValueByName("#F.creationDate", ParaVal[1]);
      }else  if(ParaVal[0] == "enable"){
      		vat.item.setValueByName("#F.enable", ParaVal[1]);
      }else if(ParaVal[0] == "isUpdate"){
      		vat.item.setValueByName("#F.isUpdate", ParaVal[1]);
      }
    }  
   }  

}
*/