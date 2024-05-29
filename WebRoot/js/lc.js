function sumReturnAmount() {
	var returnAmounts = $('input[@id*=_returnAmount_]');
	var returnFeess = $('input[@id*=_returnFees_]');
	var totalReturnAmount = 0;
	var totalReturnFees = 0;
	for(var i = 0; i< returnAmounts.length; i++){
		totalReturnAmount = totalReturnAmount + parseFloat(returnAmounts[i].value);
	}
	for(var j = 0; j< returnFeess.length; j++){
		totalReturnFees = totalReturnFees + parseFloat(returnFeess[j].value);
	}
	$('input[@name^=totalReturnAmount]')[0].value = totalReturnAmount;
	$('input[@name^=totalReturnFees]')[0].value = totalReturnFees;
}