<%@ page contentType="text/html; charset=UTF-8" %>
 <!-- saved from url=(0043)http://localhost:9090/erp/showFileImage.jsp -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>

<HEAD>

<link rel="stylesheet" type="text/css" href="css/login.css">

</HEAD>


<BODY>
  <div class="images">
  <div class="wrapper fadeInDown">
  <div id="formContent">
    <!-- Tabs Titles -->
    <h2 class="active"> Sign In </h2>

   
    <!-- Login Form -->
    <form accept-charset='UTF-8' id='main' method='POST' action="http://60.250.137.187:58083/erp_dev_20210702/productionsearch.jsp" 
    	enctype='application/x-www-form-urlencoded' onSubmit=''>
      <input type="text" id="login" class="fadeIn second" name="login" placeholder="請輸入您的手機號碼">
      <input type="submit" class="fadeIn fourth" value="Log In" >
      <input type="text" id="brandCode" class="fadeIn second" name="brandCode" value="T2" style="display: none;"/>
      <input type="text" id="categoryType" class="fadeIn second" name="categoryType" value="1" style="display: none;"/>
    </form>

    <!-- Remind Passowrd -->
   

  </div>
</div>
</div>
</BODY>

</HTML>
