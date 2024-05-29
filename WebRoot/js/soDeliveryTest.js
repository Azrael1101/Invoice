/*** 
 *	檔案: soDeliverySearch.js
 *	說明：入提單查詢
 *	修改：Anber
 *  <pre>
 *  	Created by Anber
 *  	All rights reserved.
 *  </pre>
 */
vat.debug.disable();
function kweBlock(){
  kweHeader();
}


function kweHeader(){ vat.block.create(vnB_Header = 1, {
	id: "vatBlock_Head", generate: true,
	table:"cellspacing='1' class='default' border='0' cellpadding='2'",
	title:"入提單號測試作業", rows:[  
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryNo1"                , type:"LABEL"  , value:"入提單號一"}]},	 		 
	 {items:[{name:"#F.deliveryNo1"                , type:"TEXT" , size:14, eChange:"checkDeliveryNo(1)"}]}]},
		 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryNo2"                , type:"LABEL"  , value:"入提單號二"}]},	 		 
	 {items:[{name:"#F.deliveryNo2"                , type:"TEXT" , size:14, eChange:"checkDeliveryNo(2)"}]}]},
	 {row_style:"", cols:[
	 {items:[{name:"#L.deliveryNo3"                , type:"LABEL"  , value:"入提單號三"}]},	 		 
	 {items:[{name:"#F.deliveryNo3"                , type:"TEXT" , size:14, eChange:"checkDeliveryNo(3)"}]}]}
	  ], 	 
		beginService:"",
		closeService:""			
	});
}


function checkDeliveryNo(vsType){
    var vsDeliveryNo = "";
	var vsResult=false;
	var vsCheckNo="";
	var vsValue =0;
	var vsCheckNo ="";
	var vsYear=0;
	var vsMonth =0;
	var vsSerial1=0;
	var vsSerial2=0;
	var vsSerial3=0;
	if(1==vsType)
	 	vsDeliveryNo = vat.item.getValueByName("#F.deliveryNo1");
	else if(2==vsType)
		vsDeliveryNo = vat.item.getValueByName("#F.deliveryNo2");
	else
		vsDeliveryNo = vat.item.getValueByName("#F.deliveryNo3");
		
	if(vsDeliveryNo.length ==13){
		//0123456789012
		//DZN0110000062
   		if(vsDeliveryNo.substr(0, 3)=="DZN"){
			vsYear   = parseInt(vsDeliveryNo.substr(3, 1),10);	
			vsMonth  = parseInt(vsDeliveryNo.substr(4, 2),10);	
			vsSerial1= parseInt(vsDeliveryNo.substr(6, 2),10);	
			vsSerial2= parseInt(vsDeliveryNo.substr(8, 2),10);	
			vsSerial3= parseInt(vsDeliveryNo.substr(10, 2),10);	
			vsCheckNo= vsDeliveryNo.substr(12, 1);	
			
			vsValue=parseInt((vsSerial1+vsSerial2+vsSerial3)/(vsYear+vsMonth+vsSerial3)*1000,10).toString();
			if(vsCheckNo == vsValue.substr(vsValue.length-1,1))
				vsResult=true;
			else
				alert("入提單號("+vsDeliveryNo+")檢查碼錯誤")
		}else{
			alert("入提單號("+vsDeliveryNo+")前置碼錯誤")
		}
		
	}else{
		alert("入提單號("+vsDeliveryNo+")長度不足")
	}
		
}



