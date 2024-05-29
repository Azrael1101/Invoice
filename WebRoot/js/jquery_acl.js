function doAcl(functionCode,debug) {
	if( (functionCode == "") || (functionCode == undefined) ){
		var subStrB = location.href.lastIndexOf("/") + 1 ;
		var subStrE = location.href.lastIndexOf("?") ;
		if( subStrE > 0 ){
			functionCode = location.href.substring( subStrB , subStrE );
		}else{
			functionCode = location.href.substring( subStrB );
		}		
	}
		
	if( debug )
		alert("functionCode " + functionCode);
		
	$.ajax({
		type: "POST",
		url: "./servlet/AccessControlServ",
		dataType: "xml",
		data: "rFunctionCode=" + functionCode,
		success: function(xml){		
			$(xml).find("control").each(function(i){
            	var t1=$(this).children("objectCode").text();
            	var t2=$(this).children("objectName").text();
            	var t3=$(this).children("objectType").text();
            	var t4=$(this).children("controlType").text().toUpperCase();
				var objId = "#" + t1 ;
				var accessObj = $(objId).children().get(0) ;
				if( debug )
					alert( "debug mode t1 : " + t1 + " -> " + accessObj + " = " + t4  + " = " + typeof(accessObj) + " = " + accessObj.tagName + " = " + accessObj.id ) ;
				if( t4 == "H" ){
					//vat.form.item.hidden( accessObj.id ) ;
					vat.form.item.hidden(t1);	
				}else if( t4 == "D" ){
					//vat.form.item.disable( accessObj.id ) ;
					vat.form.item.disable(t1) ;
				}
								
        	});
		}
	});
}