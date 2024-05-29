/***
 * 檔案：vat.js 
 * 說明：系統宣告及預設 
 * <pre>
 * 	Created by Mac  
 * 	All rights reserved.
 * </pre>
 */

// A simple way to check for HTML strings or ID strings
// (both of which we optimize for)
var quickExpr = /^[^<]*(<(.|\s)+>)[^>]*$|^#(\w+)$/,
// Is it a simple selector
		isSimple = /^.[^:#\[\.]*$/,
// Will speed up references to undefined, and allows munging its name.
		undefined;
	
__vat = function(){
	this.$version = '1.0.3';
	this.$ = function(){};
	this.userAgent = navigator.userAgent.toLowerCase();
	this.browser = {
		version: (this.userAgent.match(/.+(?:rv|it|ra|ie)[\/: ]([\d.]+)/) || [])[1],
		safari: /webkit/.test(this.userAgent),
		opera: /opera/.test(this.userAgent),
		msie: /msie/.test(this.userAgent) && !/opera/.test(this.userAgent),
		mozilla: /mozilla/.test(this.userAgent) && !/(compatible|webkit)/.test(this.userAgent)
	};						 
};
vat = new __vat();
vat.$ = function vat$(element) {
  if (arguments.length > 1) {
    for (var i = 0, elements = [], length = arguments.length; i < length; i++)
      elements.push(vat$(arguments[i]));
    return elements;
  }
  if (document.getElementById)
    element = document.getElementById(element);
  return element;
}
vat.$default = function vat$default(){
	for (var i = 0; i < arguments.length; i++){ 
		if (typeof arguments[i] !== "undefined"){
			return arguments[i];
		}
	}			 		
	return "";
};
vat.$extend = function vat$extend(destination, source){
	if (typeof destination === "undefined" || destination === null){
		destination = source instanceof Array ? [] : {};
	}
	for (var property in source){
		destination[property] = source[property];
	}	
	return destination;
}
vat.jr = function vatJsRun(psJsCode, pxResult){
	var vxResult;
	function defaultResult(vsType, e){
		if (typeof pxResult === "undefined"){
			vxResult = psJsCode;
			vat.debug("developer", "vat.jr 執行"+vsType+"警告, 錯誤的函數("+psJsCode+"), 且預設值是 undefined, 錯誤訊息為:"+e, vat.jr.caller);
		}else{
			vxResult = pxResult;
		}	
	}
	if (typeof psJsCode === "function"){
		try{
			vxResult = psJsCode();
		}catch(e){
			defaultResult("function", e);
		}	
	}else{
		if (typeof psJsCode === "string"){
			try{
				vxResult = eval(psJsCode);
		 	}catch(e){
		 		defaultResult("eval", e);
			}		
		}else{
			defaultResult("", "");
		}
	}
	return vxResult;	
};

vat.$jst = function vatJsT(pxJsCode, pnTime, poOpt){
	var vnTimerId;
	vnTimerId = setTimeout(function(){
		clearTimeout(vnTimerId);	
		vat.$jsr(pxJsCode, poOpt);
	}, typeof pnTime === "number" ? pnTime : 100);
}

vat.$that = function(poOpt){
	return (poOpt && poOpt.that ? poOpt.that :
				  event !== null && event.srcElement !== null ?
				 	event.srcElement.nodeType === 3 ? event.srcElement.parentNode : event.srcElement :
				 	document);   // check if event.srcElement is a textnode (safari) or null 	
}

// Javascript Runtime execute
vat.$jsr = function vatJsR(pxJsCode, poOpt, pxDefaultResult){
	var vxResult;
	switch (typeof pxJsCode){
		case "function":
			vxResult = pxJsCode(vat.$that(poOpt));
			break;
		case "string":	
			var voThat = vat.$that(poOpt);
			vxResult = eval(pxJsCode);
			break;
		case "undefined":
			break;	// 沒有要執行任何 script			
		default:
			vat.debug("developer", "【vat.$jsr 執行錯誤】錯誤的函數型態("+(typeof pxJsCode)+")"+
								poOpt && poOpt.description ? "\n\n 事件描述:"+poOpt.description : "");
	}
	if (typeof vxResult === "undefined"){
		if (pxDefaultResult)
			vxResult = pxDefaultResult;	// specific argument for default value
		else if (poOpt && typeof poOpt.defaultResult !== "undefined")
			vxResult = poOpt.defaultResult;	 
	}
	return vxResult;
};
vat.$addEvent = function vat$addEvent(elem, type, handler){
	var voHandler, voPrior
	if (typeof elem === "object"){
		voPrior = elem["on" + type];
		if(elem.nodeType == 3 || elem.nodeType == 8)	return;
		if(vat.browser.msie && elem.setInterval) elem = window;	// IE
		elem["on" + type] = function(){
			if(typeof voPrior === "function")	vat.$jsr(voPrior, {that : this}); 
			vat.$jsr(handler, {that : this}); 
		};	
	}else{
		vat.debug("developer", "【物件錯誤】無法加入事件"); 
	}
	/*
	if (elem.addEventListener)
		elem.addEventListener(type, voHandler, false);
	else if (elem.attachEvent)
		elem.attachEvent("on" + type, voHandler);
	*/
	// else if (typeof handler === "string") elem["on" + type] = function(){ vat.jr(handler); };				
	// elem = null;	// Nullify elem to prevent memory leaks in IE
}
vat.$addLoadEvent = function vat$addLoadEvent(pfcall){
	var oldonload = window.onload;
	if(typeof window.onload !== "function"){
		window.onload = pfcall;
	}else{
		window.onload = function(){
			if (oldonload){
				oldonload();
			}
			pfcall();
		}	 
	}
}
vat.$clone = function vat$cloneObject(poSource) {
	function cloneObject(source){
		for(i in source){
			if(typeof source[i] == 'source'){
				this[i] = new cloneObject(source[i]);
			}else{
				this[i] = source[i];
			}
		}
	}
	var voResult = new cloneObject(poSource);
}	
vat.$arrayCopy = function vat$arrayCopy(paArray){
	function newArray(paArray){
		var vaArray = [];
		for (var i=0; i < paArray.length; ++i)
			vaArray[i] = paArray[i] instanceof Array ? newArray(paArray[i]) : paArray[i];
		return vaArray;
	}
	return newArray(paArray);
};
vat.$copy = function vat$copy(paObject){
	function newObject(paObject){
		var vaObject = [];
		for (var vsProperty in paObject)
			vaObject[vsProperty] = paObject[vsProperty] instanceof Object ? newObject(paObject[vsProperty]) : paObject[vsProperty];
		return vaObject;
	}
	return newObject(paObject);
};
vat.$caller = function vatCallerName(psCaller){
	var vsCaller = psCaller.toString();
	return vsCaller.substring(vsCaller.indexOf("func", 0)+8, vsCaller.indexOf("(", 0))+"()";
};



//*** start vat ***
vat.$addLoadEvent(function(){
	// re-define debug
	vat.debug = function(psLevel, psMsg){
		switch(psLevel){
		case "user":
			if (vat.debug.$box.user) 
				window.alert("訊息通知："+psMsg);
			break;
		case "developer":
			if (vat.debug.$box.developer)	
				window.alert("訊息通知："+psMsg);
			break;
		case "vat":
			if (vat.debug.$box.vat) 
				window.alert("訊息通知："+psMsg);
			break;
		case "isCeapDesignStudio":
			var vsMsg, voMsgTag = document.getElementsByName("#vatMessage");
			if (typeof voMsgTag[0] === "object"){
				voMsgTag[0].style.display = "none";	//*** 先隱藏此元素
				vsMsg = voMsgTag[0].value; 
				if (typeof vsMsg == "string" && vsMsg === '[binding]')
					return true;	//*** 在 ceap studio 中無法取得 #vatMmessage 元素
			}
			break;
		}
		return false;
	};
	vat.debug.$box = {vat : false, developer : false, user : true};
	vat.debug.enable = function(psLevel){
		switch(psLevel){
		case "developer": 
			vat.debug.$box.developer = true; 
			break;
		case "vat": 
			vat.debug.$box.vat = true; 
			break;
		default:	
			vat.debug.$box.developer = true;
			vat.debug.$box.vat = true;			
		}	
	};
	vat.debug.disable = function(psLevel){
		switch(psLevel){
		 case "developer":
			vat.debug.$box.developer = false;
			break;
		case "vat":
			vat.debug.$box.vat = false;
			break;
		default:
			vat.debug.$box.developer = false;
			vat.debug.$box.vat = false;			
		}
	};
	vat.debug("isCeapDesignStudio");
	window.onresize = function (){if (typeof vat.tabm != 'undefined')	vat.tabm.flash();};
	if(typeof vat.tabm != 'undefined') vat.tabm.complete();
	if(typeof vat.form != 'undefined') vat.form.complete();
	vat.$load = "finish";
});
