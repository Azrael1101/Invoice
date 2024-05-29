/*** 
 *	檔案: buCountry.js
 *	說明：表單明細
 */

vat.debug.disable();

var vnB_Button = 0;
var vnB_Header = 1;

function outlineBlock(){
  	formInitial();
  	buttonLine();
  	headerInitial();
	
}


function formInitial(){ 
	vat.bean.init(	
	  		function(){
					return "process_object_name=buCommonPhraseAction&process_object_method_name=performInitial"; 
	    	          },{
	    		other: true
	    	            }
	);
 
}

function buttonLine(){
    var vsMaxRecord     = 0;
    var vsCurrentRecord = 0;
    vat.block.create(vnB_Button, {
	id: "vatBlock_Button", generate: true,
	//table:"cellspacing='0' class='default' border='0' cellpadding='0' style='border-width: 5px; border-left-width: 5px; border-color: #ffffff;'",	
	title:"", rows:[  
	 {row_style:"", cols:[
	 	{items:[{name:"#B.submit"      , type:"IMG"    ,value:"送出",   src:"./images/button_submit.gif", eClick:'doSubmit("SUBMIT")'}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
//	vat.item.setValueByName("#L.currentRecord", vat.bean().vatBeanOther.currentRecordNumber);
//	vat.item.setValueByName("#L.maxRecord"    , vat.bean().vatBeanOther.lastRecordNumber);
}	

function headerInitial(){ 
	vat.block.create( vnB_Header, { //vnB_Header = 
		id: "vatBlock_Head", generate: true,
		table:"cellspacing='1' class='default' border='0' cellpadding='2'",
		title:"KWE跑馬燈設定", 
		rows:[
			{row_style:"", cols:[
				{items:[{name:"#L.countryCode", type:"LABEL", value:"是否啟用<font color='red'>*</font>" }]},
				{items:[{name:"#F.enable", type:"CHECKBOX",  bind:"enable" },
						{name:"#L.enable", type:"LABEL",value:"停用?", size:10 }],td:" colSpan=2"}
			]},
			{row_style:"", cols:[
				{items:[{name:"#L.attribute1", type:"LABEL", value:"公告事項", row:2, col: 40}], td:" rowSpan=2"},
				{items:[{name:"#F.attribute1", type:"TEXTAREA", bind:"attribute1", size:50 , row:10, col: 50}], td:" rowSpan=2"}
			]}
			
		], 	
		 
		beginService:"",
		closeService:""			
	});
}

// 送出,暫存按鈕
function doSubmit(formAction){
	
	var alertMessage ="是否確定送出?";
	
	if(confirm(alertMessage)){
	    vat.bean().vatBeanOther.formAction 		= formAction;
		vat.block.submit(function(){
				return "process_object_name=buCommonPhraseAction"+
				"&process_object_method_name=performMarqueeTransaction";
			},{
				bind:true, link:true, other:true 
			}
		);
	}
}
