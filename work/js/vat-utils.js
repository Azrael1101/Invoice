/***
 *	檔案：vat-utils.js 
 *	說明：開放外部建立的共用函數
 *	修改：
 *  <pre>
 *  	Created by Mac
 *  	All rights reserved.
 *  </pre>
 */

//*** vat & vat.utils 宣告
	if (typeof vat === "undefined") vat = function(){};
	vat.utils = {};
	
//*** 尋找元素在陣列中是否存在
vat.utils.find = function (pString, pArray) {
	if ((pArray instanceof Array) && typeof (pString) == "string") {
		for (i in pArray) {
			// alert("find array[" + i + "] : " + pArray[i]);
			if (typeof (pArray[i]) == "string" && pArray[i] == pString) {
				return i;
			}
		}
	}
	return false;
};
//*** 尋找元素在頁面中的相對座標
vat.utils.findPos = function(pObj){
	var obj = pObj, curleft = 0, curtop = 0;
	if (obj.offsetParent){
		do{
			if (obj.style.left) curleft += parseInt(obj.style.left);
			 							else 	curleft += obj.offsetLeft;
			if (obj.style.top)	curtop 	+= parseInt(obj.style.top);
										else	curtop 	+= obj.offsetTop;
		}while((obj = obj.offsetParent));
	}
	return [curleft, curtop];
};

vat.utils.getAbsolutePos = function(pObj){
	var obj = pObj, curleft = 0, curtop = 0;
	if (obj.offsetParent){
		do{
			curleft += obj.offsetLeft;
			curtop 	+= obj.offsetTop;
		}while((obj = obj.offsetParent) && obj.style.position != "absolute");
	}
	return [curleft, curtop];
};

//*******************************************************
//*** below created by shan
vat.utils.strToArray = function (pString) {
	//alert( "pString=" + pString );
	re = pString.split("{S}");
	return re;
};
vat.utils.arrayToStr = function (pArray) {
	var reStr = "";
	for (i = 0; i < pArray.length; i++) {
		reStr = reStr + pArray[i] + "{S}";
	}
	reStr = reStr.substr(0, reStr.length - 3);
	return reStr;
};
/*******************************
	功能：將String to 2D Array
	範例：[233][0]=CO1279KHMML{S}[233][1]=塗鴉帽子
	說明
*/
vat.utils.strTwoDArray = function (pString, startRow, column, row) {
	//alert( "strTwoDArray -> startRow=" + startRow + ",column=" + column + ",row=" + row + ",pString =" + pString) ;
	if (typeof startRow === "number" && typeof row === "number" && typeof column === "number"){ 
		var endRow = parseInt(startRow) + parseInt(row);
		var re = new Array(row);
		var startPos = 0;
		var endPos = 0;
		if (pString.length > 0) {
			for (i_row = startRow; i_row < endRow; i_row++) {
				re[i_row] = new Array(column);
				for (i_column = 0; i_column < column; i_column++) {
					searchKey = "[" + i_row + "][" + i_column + "]=";
					//alert( "searchKey " + searchKey ) ;
					startPos = pString.indexOf(searchKey) + searchKey.length;
					//alert( "startPos " + startPos ) ;
					endPos = pString.indexOf("{S}", startPos);
					//20090113 shan add				
					if (endPos < 0) {
						endPos = pString.length;
					}
					re[i_row][i_column] = pString.substring(startPos, endPos);
					if ((endPos + "{S}".length) >= (pString.length)) {
						break;
					}
				}
				if ((endPos + "{S}".length) >= (pString.length)) {
					break;
				}
			}
		}	
	}else
		vat.debug("developer", "回傳的數值參數錯誤");
	return re;
};
/*******************************
	功能：將String to Input 2D Array
	範例：[0][0]=ObjName{S}[0][1]=DefaultValue{S}[0][2]=boolean{S}[1][0]=show{S}[2][0]=value{S}[1][1]=show{S}[2][1]=value{S}[1][2]=show{S}[2][2]=value{S}[1][3]=show{S}[2][3]=value
	說明
*/
vat.utils.strTwoInputDArray = function (objectName, objectDefaultValue, option, pString) {
	//alert("strTwoInputDArray -> objectName =" + objectName + ",objectDefaultValue=" + objectDefaultValue + ",option=" + option + ",pString=" + pString);
	var col = vat.utils.strMaxCol(pString);
	var endCol = parseInt(col) + 1;
	var re = new Array(endCol);
	var startPos = 0;
	var endPos = 0;
	re[0] = new Array(3);
	re[0][0] = objectName;
	re[0][1] = objectDefaultValue;
	re[0][2] = option;
	if (pString.length > 0) {
		for (i_row = 1; i_row < 3; i_row++) {
			re[i_row] = new Array(2);
			for (i_column = 0; i_column < endCol; i_column++) {
				searchKey = "[" + i_row + "][" + i_column + "]=";
				//alert( "searchKey " + searchKey ) ;
				startPos = pString.indexOf(searchKey) + searchKey.length;
				//alert( "startPos " + startPos ) ;
				endPos = pString.indexOf("{S}", startPos);
				if (endPos < 0) {
					endPos = pString.length;
				}
				re[i_row][i_column] = vat.utils.unescape(pString.substring(startPos, endPos)); // add unescape to decode, by mac
				//alert( " startPos=" + startPos + ",endPos=" + endPos + " re1 " + i_row + "-" + i_column + "->" + re[i_row][i_column] );
			}
		}
	} else {
		if (endCol < 3) {
			endCol = 3;
		}	
		re[1] = new Array(2);
		re[2] = new Array(2);
	}
	//alert("strTwoInputDArray re array is " + re);
	return re;
};
/*******************************
	功能：取得最大的ROW
	範例：[0][0]{S}[0][1]{S}[1][0]{S}[1][1]{S}[2][1]{S}[3][1]{S}[1][1]{S}[100][1]
	說明
*/
vat.utils.strMaxRow = function (pString) {
	var startPos = pString.lastIndexOf("{S}");
	startPos = pString.indexOf("[", startPos);
	startPos = startPos + 1;		
	//alert( "spos =" + startPos ) ;	
	var endPos = pString.indexOf("]", startPos);	
	//alert( "epos =" + endPos ) ;
	var returnMax = pString.substring(startPos, endPos);
	if (returnMax != "") {
		return parseInt(returnMax);
	} else {
		return 0;
	}
};
/*******************************
	功能：取得最大的Col
	範例：[0][0]=00{S}[0][1]=01{S}[1][0]=10{S}[1][1]=11{S}[2][1]=21{S}[1][2]=12{S}[2][2]=22{S}[1][3]=13{S}[2][3]=32
	說明
*/
vat.utils.strMaxCol = function (pString) {
	//alert("strMaxCol pString=" + pString);
	var startPos = pString.lastIndexOf("[");
	startPos = startPos + 1;		
	//alert( "spos =" + startPos ) ;	
	var endPos = pString.lastIndexOf("]");	
	//alert( "epos =" + endPos ) ;
	var returnMax = pString.substring(startPos, endPos);
	//alert("returnMax =" + returnMax);
	if (returnMax != "") {
		return parseInt(returnMax);
	} else {
		return 0;
	}
};

/*******************************
	功能：將2D Array to String
	範例：[233][0]=CO1279KHMML,[233][1]=塗鴉帽子
	說明
*/

vat.utils.escape = function vatBean_escape(psStr){
	// return psStr;
	return encodeURIComponent(psStr);
	// return encodeURI(psStr);
	// return escape(psStr);
};
vat.utils.unescape = function vatBean_unescape(psStr){
	// return psStr;
	return decodeURIComponent(psStr);
	// return decodeURI(psStr);	
	// return unescape(psStr);
};

vat.utils.escapeURL = function vatBean_escape(psStr){
	// return psStr;
	// return encodeURIComponent(psStr);
	// return encodeURI(psStr);
	return escape(psStr);
};
vat.utils.unescapeURL = function vatBean_unescape(psStr){
	// return psStr;
	// return decodeURIComponent(psStr);
	// return decodeURI(psStr);	
	return unescape(psStr);
};



vat.utils.twoDArrayToStr = function (pArray, startRow) {
	//alert( "傳進來的參數 pArray-> " + pArray ) ; 
	if (pArray != null) {
		//alert( "startRow=" + startRow + " ,pArray.length=" + pArray.length + " ,pArray=" + pArray) ;
		var re = "";
		for (i = startRow; i < pArray.length; i++) {
			//alert(" pArray[" + i + "].length =" + pArray[i].length);
			for (j = 0; j < pArray[i].length; j++) {
				if (typeof pArray[i][j] != "undefined") {
					re = re + "[" + i + "][" + j + "]=" + vat.utils.escape(pArray[i][j]) + "{S}";			// add escape to encode, by mac
					//alert( re ) ;
				}
			}
		}
		re = re.substr(0, re.length - 3);
	}
	//alert("return re=" + re);
	return re;
};

/*
	取得AJAX錯誤訊息
	true : 正確
	flase : 錯誤	
*/
vat.utils.errorMsgResponse = function (responseText){
	var responseStatus = vat.ajax.getValue("status", responseText);
	var responseMsg = vat.ajax.getValue("msg", responseText);		
	if( "Error" == responseStatus ){
		return responseMsg ;
	}else{
		//是否要做ALERT
		return "" ;
	} 
};

function cheng(num, n) {
	var dd = 1;
	var tempnum;
	for (i = 0; i < n; i++) {
		dd *= 10;
	}
	tempnum = num * dd;
	tempnum = Math.round(tempnum);
	return tempnum ;
};

function ForDight(Dight, How) {
	Dight = Math.round(Dight * Math.pow(10, How)) / Math.pow(10, How);
	return Dight;
};
