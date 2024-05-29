/*** 
 *	檔案: buGoalItemCount.js
 *	說明：商品折數,抽成率維護
 */
 
 /*** 
 *	檔案: buCountry.js
 *	說明：表單明細
 */

vat.debug.disable();
var afterSavePageProcess = "";
var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	formInitial();
  	headerInitial();
  	buttonLine();
	doFormAccessControl();
}


function formInitial(){
 
	if(typeof document.forms[0]["#loginBrandCode"] != 'undefined'){
   	 	 vat.bean().vatBeanOther =
        { 
          loginBrandCode  		: document.forms[0]["#loginBrandCode" ].value,
          loginEmployeeCode  	: document.forms[0]["#loginEmployeeCode" ].value,
          formId             	: document.forms[0]["#formId"            ].value,
          lineId				: document.forms[0]["#lineId"           ].value,
		  headId				: document.forms[0]["#headId"           ].value,
          currentRecordNumber 	: 0,
	      lastRecordNumber    	: 0
        };
        
	  	vat.bean.init(	
	  		function(){
					return "process_object_name=buPurchaseAction&process_object_method_name=performInitialwork"; 
	    	},{
	    		other: true
	    	}
	    );
  }
  
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[
	 			{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 	 		{name:"#B.exit"        , type:"IMG"    ,value:"離開",   src:"./images/button_exit.gif"  ,eClick:'closeWindows("CONFIRM")'},
	 	 		{name:"SPACE"          , type:"LABEL"  ,value:"　"},
	 			{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'},
	 			{name:"submitSpace"	   , type:"LABEL"  ,value:"　"}],
	 			  td:"style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){ 
    var allstatus   = [[true,true,true,true], ["完成","未完成","不處理"],["完成","未完成","不處理"]];
    var alltaskType   = [[true,true,true,true], ["SA","EXCLE","TEST","ONLINE"],["SA","EXCLE","TEST","ONLINE"]];
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"工作狀況", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.no", type:"LABEL", value:"主指名稱<font color='red'>*</font>"}]},
				{items:[{name:"#F.no", type:"TEXT", bind:"no", size:25, maxLen:25 }]},
				{items:[{name:"#L.status", type:"LABEL", value:"狀態<font color='red'>*</font>"}]},
				{items:[{name:"#F.status", type:"SELECT", bind:"status", size:25, maxLen:25,init:allstatus  }]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.specInfo", type:"LABEL" , value:"工作項目<font color='red'>*</font>"}]},
				{items:[{name:"#F.specInfo", type:"TEXT", bind:"specInfo", size:50, maxLen:25 }], td:" colSpan=3"}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.taskInchargeCode", type:"LABEL", value:"執行人員"}]},
				{items:[{name:"#F.taskInchargeCode", type:"TEXT", bind:"taskInchargeCode", size:10, maxLen:25, eChange:"eChangeRequest()" },
						{name:"#F.taskInchargeName", type:"TEXT", bind:"taskInchargeName", size:10, maxLen:25, eChange:"eChangeRequestCode()"}]},
						
				{items:[{name:"#L.taskType", type:"LABEL" , value:"分類階段"}]},
				{items:[{name:"#F.taskType", type:"SELECT", bind:"taskType",init:alltaskType}]}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.estimateStartDare", type:"LABEL", value:"預計日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.estimateStartDare", type:"DATE", bind:"estimateStartDare", size:10, maxLen:25 }]},
				{items:[{name:"#L.finishDate", type:"LABEL", value:"執行日期<font color='red'>*</font>"}]},
				{items:[{name:"#F.finishDate", type:"DATE", bind:"finishDate", size:10, maxLen:25 }]}
			]},
			
			{row_style:"", cols:[
				{items:[{name:"#L.executeTimeStart", type:"LABEL", value:"開始時間"}]},
				{items:[{name:"#F.executeTimeStart", type:"TIME", bind:"executeTimeStart", size:10, maxLen:25 }]},
				{items:[{name:"#L.executeHours", type:"LABEL", value:"工時"}]},
				{items:[{name:"#F.executeHours", type:"TEXT", bind:"executeHours", size:10, maxLen:25 }]}
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
	
	var alertMessage ="是否確定送出?";
	
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 		= formAction;
		vat.block.submit(function(){
				return "process_object_name=buPurchaseAction"+
				"&process_object_method_name=performTransactionAdvance";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}

// 當picker按下檢視回來時,執行
function doAfterPickerProcess(){
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
		  var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1 ].achevement_Id;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].achevement_Id;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].achevement_Id;
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
	  	var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].achevement_Id;
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
	    var code = vat.bean().vatBeanPicker.result[vat.bean().vatBeanOther.currentRecordNumber -1].achevement_Id;
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
	//document.forms[0]["#formId"            ].value = code; 
	//vat.bean().vatBeanOther.formId       = document.forms[0]["#formId"].value;	
	
	vat.block.submit(
		function(){
			return "process_object_name=buPurchaseAction&process_object_method_name=performInitialwork"; 
     	},{other      : true, 
     	   funcSuccess:function(){
     			vat.item.bindAll(); 
				doFormAccessControl();
     	}}
    );
    	
}

// 依formId鎖form

function doFormAccessControl(){
/*
	var formId = vat.bean().vatBeanOther.formId;
//	alert(typeof formId);
	if( formId != "" ){
		vat.item.setAttributeByName("#F.achevement", "readOnly", true);
		
	}else{
		vat.item.setAttributeByName("#F.achevement", "readOnly", false);
	}
	
	var enable = vat.item.getValueByName("#F.enable"); 
//	alert("enable = " + enable);
	if(enable == "Y" ){
		vat.item.setValueByName("#F.enable", "N");
	}else{
		vat.item.setValueByName("#F.enable", "Y");
	}
	*/
}

 