//document.domain = "myweb.hinet.net";
var Get_Date = new Date();
var GenerateTime = ''+ Get_Date.getFullYear()+ Get_Date.getDate()+'' + Get_Date.getTime();
var MainPageURL = location.href;
var AdPageURL;
var AD_width;
var scroll_type;
var ADPage = new Object;
var InterId;
var caseTmp;
var tmp_time = Get_Date.getTime()%10;
var Reload_AD_Page;

//check cookie exist
function getCookie(Get_Name)
{       	     
       var arg = escape(Get_Name);
			 var NameLen = arg.length;
			 var CookieLen = document.cookie.length;
			 var i = 0;
	     while (i < CookieLen) {
		   		var j = i + NameLen;
		   		if (document.cookie.substring(i, j) == arg){
		   			 return getCookieValue(j+1); //return cookie value
			    }   
		      i = document.cookie.indexOf(" ", i) + 1;
		      if (i == 0){break;}
	     }
	     return null;  //not found cookie return null       
}

//set cookie
function saveCookie(Set_Name){ 
       var Set_Cookie_Date = new Date();
       var StrCookie = Set_Name + "=" + Set_Cookie_Date.getSeconds();                        
       StrCookie +="; path="+ "/";
       StrCookie += "; domain="+ "myweb.hinet.net";          
       document.cookie=StrCookie;      
}

//get cookie value
function getCookieValue(StartIndex) {
       var EndIndex = document.cookie.indexOf(";", StartIndex);
       if (EndIndex == -1) 
             EndIndex = document.cookie.length;
       return unescape(document.cookie.substring(StartIndex, EndIndex));
}

//reload AD page function
function doReload(myFrame, newURL) {
                   myFrame.location.replace(newURL);                 
}
                
function reloadFrames() {
			 var Now_Window = window;
    	 while (Now_Window != top){
			 		 		try{
		        	 		size = Now_Window.frames.length;
		        	 		var frameids = Now_Window.document.getElementsByTagName('frame');
		
		        	 		for (i = 0; i < size; i++) {
		           				 if (frameids[i].src.match(Reload_AD_Page) != null){ 		           				 	 		
		            		      	doReload(Now_Window.frames[i], frameids[i].src);
		             		      	return true;		             		      
		             		   }		               		   	  
		          	  }
	            		Now_Window = Now_Window.parent;              	
  		   		   }catch(err){  				   		  				   			
	    			  		Now_Window = Now_Window.parent;
  		  	     }  				    
  		 }  			  
  		 if (Now_Window == top)
  		 {
  		    	  try{ 
        	  	   size = Now_Window.frames.length;  
        	  	   var frameids = Now_Window.document.getElementsByTagName('frame');
            
        	  	   for (i = 0; i < size; i++) {
            			  if (frameids[i].src.match(Reload_AD_Page) != null){               		    
		            		      	doReload(Now_Window.frames[i], frameids[i].src); 
		             		      	return true;
		             		}
                 }             
  		   	    }catch(err){
  		   	    }
 		   }  
}     
function activeReload(){	  	   	   
	    if (top.document.getElementById("MywebFrameSet_160600")){
	    	Reload_AD_Page = "http://hiad.myweb.hinet.net/160_600.html";
	    }else if (top.document.getElementById("MywebFrameSet_300250")){
	    	Reload_AD_Page = "http://hiad.myweb.hinet.net/300_250.html";
	    }else{
	    	Reload_AD_Page = "http://hiad.myweb.hinet.net/120_300.html";
	    }
	   		   	
      if (window.addEventListener) //DOM method for binding an event
        window.addEventListener("load", reloadFrames, false)
      else if (window.attachEvent) //IE exclusive method for binding an event
        window.attachEvent("onload", reloadFrames)
}

//get UserPage title
function getUsertitle(){
      top.document.title = eval('top.userpage.document.title');
}

//findCookie
function findCookie(){
      var Get_Now_Date = new Date();
	  	var Diff_Time = Math.abs((Get_Now_Date.getSeconds() - Cookie_Value));
	    if (Diff_Time > 3){
	        activeReload();
	        saveCookie("MywebID");
	    }
}

//check User URL parameter type
function para_type(){
      if (MainPageURL.match(/#.*\?/)) {
            sg =  MainPageURL.split("#"); 
            var temp = sg[1].split("?");
            MainPageURL= sg[0] + '?' + temp[1] + '&' + GenerateTime + '#' + temp[0];            
            return MainPageURL;
      } else if (MainPageURL.match(/\?.*#/)) {
             if (MainPageURL.indexOf("#")>-1){
                  sg = MainPageURL.split("#");
                  MainPageURL = sg[0] + '&MywebPageId=' + GenerateTime + '#' + sg[1];
                  return MainPageURL;
             }        
      } else if (MainPageURL.match(/#/)){
             sg=MainPageURL.split("#");
             MainPageURL=sg[0]+'?MywebPageId='+GenerateTime+'#'+sg[1];
             return MainPageURL;
      } else if (MainPageURL.match(/\?/)){
             MainPageURL+= '&MywebPageId=' +  GenerateTime;
             return MainPageURL;
      }else{
             MainPageURL+="?MywebPageId=" + GenerateTime;
             return MainPageURL;
      }          
}

//Myweb pop up AD function
function OpenADPage(){
      newleft=screen.width - 300;
      ADPage=null;
	  //window.open("http://hiad.myweb.hinet.net/280x300.html","myweb_pop","height=310,width=310,top=0,left=" + newleft + ",toolbar=no,menubar=no,scrollbars=no,resizable=no,location=no,status=no");
}

function get_popup_Cookie()
{
      var strCookie;
      strCookie = document.cookie;
      nameCheck = strCookie.split(";");
      if (nameCheck[0] == "Myweb")               
          return 0;
      else if (nameCheck[0] == "MywebADPage")    
            return 1;
      else return 2;
}

function save_popup_Cookie(name, expires){
      var strCookie = name;
      if (expires){
          var curTime = new Date();
          curTime.setTime(curTime.getTime() + expires);
                     strCookie += "; expires=" + curTime.toGMTString();
          strCookie +="; path="+ "/";
          strCookie += "; domain="+ "myweb.hinet.net";
          document.cookie=strCookie;
      }
}

function CheckADPage(){       
      if ((ADPage.closed) && (caseTmp != 1)){//pop up ad close and cookie isn't MywebADPage
          save_popup_Cookie("MywebADPage",150000); //this number is test
          clearInterval(InterId);
      }
}

function myweb_popAD(){	     
      caseTmp=get_popup_Cookie();
      InterId=setInterval("CheckADPage()",1000);          
      switch (caseTmp)
		  {
          case 0 ://cookie name is Myweb
             break;
			    case 1 ://cookie name is MywebADPage
     		     OpenADPage();
             ADPage.blur();
             break;
          default ://not get anything cookie name or cookie name isn't Myweb or MywebADPage
             OpenADPage();
             ADPage.focus();
             save_popup_Cookie("Myweb",5000);
             break;
      }
}
   
if (top == self){
	        MainPageURL = para_type();	
	        	        
	         //check user screen width to set AD_width
          if (screen.width <= "800"){          		
          		AdPageURL = "http://hiad.myweb.hinet.net/120_300.html";
          		AD_width = 120;
          		scroll_type = "no"; 
          		document.write('<frameset id="MywebFrameSet_120300" cols="*,' + AD_width + '"frameborder="0" border="0" framespacing="0" scrolling="yes"><frame id="userpage" name="userpage" noresize="noresize" frameborder="0" src=' + MainPageURL  + '><frame id="adpage" name="adpage" noresize="noresize" frameborder="0" src=' + AdPageURL + ' scrolling="'+ scroll_type +'"></frameset>');    
          }else {
          		if (tmp_time == 0){
          		AdPageURL = "http://hiad.myweb.hinet.net/300_250.html";
          		AD_width = 224;
          		scroll_type = "yes";
          		document.write('<frameset id="MywebFrameSet_300250" cols="*,' + AD_width + '"frameborder="0" border="0" framespacing="0" scrolling="yes"><frame id="userpage" name="userpage" noresize="noresize" frameborder="0" src=' + MainPageURL  + '><frame id="adpage" name="adpage" noresize="noresize" frameborder="0" src=' + AdPageURL + ' scrolling="'+ scroll_type +'"></frameset>');
              }else{          				
          				AdPageURL = "http://hiad.myweb.hinet.net/160_600.html";
          				AD_width = 160;
          				scroll_type = "no";
          				document.write('<frameset id="MywebFrameSet_160600" cols="*,' + AD_width + '"frameborder="0" border="0" framespacing="0" scrolling="yes"><frame id="userpage" name="userpage" noresize="noresize" frameborder="0" src=' + MainPageURL  + '><frame id="adpage" name="adpage" noresize="noresize" frameborder="0" src=' + AdPageURL + ' scrolling="'+ scroll_type +'"></frameset>');
          		}
          }        
	                	         	                                     	    		
    			window.onload=function(){getUsertitle();}
    			saveCookie("MywebID");    			
}else{ //not initial, self isn't top        	          
          try{ //top id is myweb
                    if (top.document.getElementById("MywebFrameSet_120300") || top.document.getElementById("MywebFrameSet_300250") ||top.document.getElementById("MywebFrameSet_160600")){
                          var Cookie_Value = getCookie("MywebID");
                          if ( Cookie_Value != null ){
                                   findCookie();
                              }else{
                                   activeReload();
                              }                   
                    }else{
                    	 //alert("this is popup");
                    	 myweb_popAD();
                    }                    
          }catch(err){ //top id isn't myweb
                myweb_popAD();
          }
}


