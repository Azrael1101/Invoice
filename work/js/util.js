
function isNumber(val) {
	var reg = /^[0-9]*$/;
	return reg.test(val);
}
function replaceAll(strOrg, strFind, strReplace) {
	var index = 0;
	while (strOrg.indexOf(strFind, index) != -1) {
		strOrg = strOrg.replace(strFind, strReplace);
		index = strOrg.indexOf(strFind, index);
	}
	return strOrg;
}
function formatNum(theObj) {
	if (theObj != "0") {
		var digit = theObj.indexOf(".");
		var int = theObj.substr(0, digit);
		var i;
		var mag = new Array();
		var word;
		if (theObj.indexOf(".") == -1) {
			i = theObj.length;
			while (i > 0) {
				word = theObj.substring(i, i - 3);
				i -= 3;
				mag.unshift(word);
			}
			return mag;
		} else {
			i = int.length;
			while (i > 0) {
				word = int.substring(i, i - 3);
				i -= 3;
				mag.unshift(word);
			}
			return mag + theObj.substring(digit);
		}
	} else {
		return theObj;
	}
}

