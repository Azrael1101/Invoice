/*** 
 *	檔案: soBonus.js
 *	說明：  獎金計算作業
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	//formInitial();
  	buttonLine();
  	headerInitial();
	//doFormAccessControl();
}

function formInitial(){
	//do nothing
}

function buttonLine(){
	var vsMaxRecord = 0;
	var vsCurrentRecord = 0;
    vat.block.create(
    	vnB_Button, 
    	{
			id : "vatBlock_Button", 
			generate : true,
			table : "cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
			title : "", 
			rows : [  
				 {
				 	row_style : "", 
				 	cols : [
					 	{
					 		items : [
					 	 		{name : "#B.exit", type : "IMG", value : "離開", src : "./images/button_exit.gif", eClick : "closeWindows('CONFIRM')"},
					 	 		{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.submit", type : "IMG", value : "獎金計算", src : "./images/button_bonus.png", eClick : "doSubmit('SUBMIT')"},
					 			{name : "SPACE", type : "LABEL", value : "　"},
					 			{name : "#B.groupAchevement", type : "IMG", value : "群達成計算", src : "./images/button_groupAchevement.png", eClick : "doSubmit('ACHEVEMENT')"}
					 	   ],
					 	   td : "style='background-color:#eeeeee; border-color:#eeeeee; border-width: 8px;'"
					 	}
					]
				 }
	  		], 	 
			beginService : "",
			closeService : ""			
		}
	);
}

function headerInitial(){
	vat.block.create(
		vnB_Header,
		{
			id : "vatBlock_Head", 
			generate : true,
			table : "cellspacing='1' class='default' border='0' cellpadding='2' style='width:25%'",
			title : "獎金計算作業",
			rows : [
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.startDate", type : "LABEL", value : "開始日期<font color='red'>*</font>"}]},
						{items : [{name : "#F.startDate", type : "DATE", bind : "startDate", size : 2}]}
					]
				},
				{
					row_style : "", 
					cols : [
						{items : [{name : "#L.endDate", type : "LABEL", value : "結束日期<font color='red'>*</font>"}]},
						{items : [{name : "#F.endDate", type : "DATE", bind : "endDate", size : 2}]}
					]
				},
				{
					row_style: "", 
					cols : [
						{items:[{name:"#L.note", type:"LABEL" , value:"<font color='red'>*</font>為必填欄位，請務必填寫。"}],td:" colSpan=4"}
					]
				}		
			], 	
			beginService:"",
			closeService:""			
		});
}

// 離開按鈕按下
function closeWindows(closeType){
	var isExit = true;
	if("CONFIRM" == closeType) isExit = confirm("是否確認離開?");
	if(isExit) window.top.close();
}

// 送出按鈕
function doSubmit(formAction){
	var startDate = vat.item.getValueByName("#F.startDate");
	var endDate = vat.item.getValueByName("#F.endDate");
	//檢察日期是否輸入...
	if(startDate == ""){
		alert('請輸入開始日期!');
		return;
	}
	if(endDate == ""){
		alert('請輸入結束日期!');
		return;
	} 
	//檢察開始日是否大於結束日...
	var sDate =  new Date(startDate);
	var eDate =  new Date(endDate);
	if((sDate.getTime() - eDate.getTime()) > 0){
		alert('開始日期不可大於結束日期!');
		return;
	}
	if(confirm("是否確定送出?")){
		if(formAction == 'SUBMIT'){
			vat.block.submit(
				function(){
					return "process_object_name=soBonusAction" + "&process_object_method_name=performSoBonusTransaction";
				},
				{bind : true, link : true, other : true}
			);
		}
		else if(formAction == 'ACHEVEMENT'){
			vat.block.submit(
				function(){
					return "process_object_name=soBonusAction" + "&process_object_method_name=performGroupAchevement";
				},
				{bind : true, link : true, other : true}
			);
		}	
	}	
}
