/***
 * 檔案：vat-config.js 
 * 說明：系統宣告及預設 
 * <pre>
 * 	Created by Mac  
 * 	All rights reserved.
 * </pre>
 */

vat = function(){
	this.$version = '1.0.3';
	this.$ = function(){ 
					 };
};		
vat.config = function(){};
vat.config.complete = function () {

	//test 用 
	var vatUseDateTime = "" ;
	
	var winW, winH;
	
	if (self.innerWidth) {
		winW = self.innerWidth;
		winH = self.innerHeight;
	} else if (document.documentElement && document.documentElement.clientWidth) {
		winW = document.documentElement.clientWidth;
		winH = document.documentElement.clientHeight;
	} else if (document.body) {
		winW = document.body.clientWidth;
		winH = document.body.clientHeight;
	}
	
	if (winW <= 1024){
		// window.open(window.location, window.name, "width=1024, height=720, toolbar=0", true);
		// window.resizeTo(1024, winH);
	}
	window.onresize = function (){vat.config.resizeWindow()};
	
	if (typeof vat.tab != 'undefined') {
		var testsTime=new Date();
		vatUseDateTime = vatUseDateTime + 'tab s ' +  testsTime.getMinutes()+":"+testsTime.getSeconds() + '\n'  ;
		vat.tab.complete();
		var testeTime=new Date();
		vatUseDateTime = vatUseDateTime + 'tab e ' +  testeTime.getMinutes()+":"+testeTime.getSeconds() + '\n'  ;
	}

	if (typeof vat.tabm != 'undefined') {		
		var testsTime=new Date();
		vatUseDateTime = vatUseDateTime + 'tabm s ' +  testsTime.getMinutes()+":"+testsTime.getSeconds() + '\n'  ;
		vat.tabm.complete();
		var testeTime=new Date();
		vatUseDateTime = vatUseDateTime + 'tabm e ' +  testeTime.getMinutes()+":"+testeTime.getSeconds() + '\n'  ;
	}	
	
	if (typeof vat.form != 'undefined') {		
		var testsTime=new Date();
		vatUseDateTime = vatUseDateTime + 'form s ' +  testsTime.getMinutes()+":"+testsTime.getSeconds() + '\n'  ;
		vat.form.complete();
		var testeTime=new Date();
		vatUseDateTime = vatUseDateTime + 'form e ' +  testeTime.getMinutes()+":"+testeTime.getSeconds() + '\n'  ;
	}
	
	if (typeof vat.message != 'undefined') {		
		var testsTime=new Date();
		vatUseDateTime = vatUseDateTime + 'message s ' +  testsTime.getMinutes()+":"+testsTime.getSeconds() + '\n' ;
		vat.message.display();
		var testeTime=new Date();
		vatUseDateTime = vatUseDateTime + 'message e ' +  testeTime.getMinutes()+":"+testeTime.getSeconds() + '\n' ;
	}
	

	if (typeof vat.message != 'undefined' && vat.message.isDesignStudio()){
		// document.write('<DIV id="vatConfig666" style="position:absolute;top:800;left:0">vat.config.inDesignStudio</DIV>');
	}
	/*
		var x = document.getElementsByTagName("A");
		for (i in x){
			if (x[i])
				if (x[i].className)
					if (x[i].className == "columnHeaderLink"){
						x[i].style.color = "white";
					}
		}
	*/
	
	//alert(vatUseDateTime) ;
}
	
vat.config.resizeWindow = function(){
	if (typeof vat.tab != 'undefined'){
		vat.tab.flash();
	}
	if (typeof vat.tabm != 'undefined'){
		vat.tabm.flash();
	}
}



function MyKeyDown(){
}


