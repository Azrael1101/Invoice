<%
/*
 *---------------------------------------------------------------------------------------
 * Copyright (c) 2010 Tasa Meng Corperation.
 * SA :
 * PG : Weichun.Liao
 * Filename : ImInventoryCount.jsp
 * Function :
 *
 * Modification Log :
 * Vers		Date			By          Notes
 * -----	-------------	--------------	---------------------------------------------
 * 1.0.0	Jun 9, 2010		Weichun.Liao	Create
 *---------------------------------------------------------------------------------------
 */
 %>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="tw.com.tm.erp.hbm.dao.*"%>
<%@page import="tw.com.tm.erp.hbm.bean.*"%>
<%@page import="tw.com.tm.erp.hbm.service.*"%>
<%@page import="tw.com.tm.erp.hbm.SpringUtils"%>
<%@page import="tw.com.tm.erp.utils.User"%>
<%@page import="java.util.*"%>
<%
try{
	// 取得登入品牌
	String brandCode = "";
	if (request.getSession().getAttribute("userObj")!=null){
		User userObj = (User)request.getSession().getAttribute("userObj");
		brandCode = userObj.getBrandCode();
	}else{
		brandCode = "T2";
	}
	System.out.print("登入品牌 ::: " + brandCode);
%>
<html>
<link href="../css/erp.css" rel="stylesheet" type="text/css">
<head>
<title>商品資料轉換作業</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href='../css/calendar-system.css' rel='stylesheet'>
<link href='../css/default.css' rel='stylesheet'>
<link href='../css/erp.css' rel='stylesheet'>
<script type="text/javascript" src="../js/json2.js"></script>
<script type="text/javascript" src="../js/vat.js"></script>
<script type="text/javascript" src="../js/vat-message.js"></script>
<script type="text/javascript" src="../js/vat-utils.js"></script>
<script type="text/javascript" src="../js/vat-tab-m.js"></script>
<script type="text/javascript" src="../js/vat-ajax.js"></script>
<script type="text/javascript" src="../js/vat-form.js"></script>
<script type="text/javascript" src="../js/vat-block.js"></script>
<script type="text/javascript" src="../js/default.js"></script>
<script type="text/javascript" src="../js/calendar.js"></script>
<script type="text/javascript" src="../js/calendar-setup.js"></script>
<script type="text/javascript" src="../js/calendar-zh.js"></script>
<script type="text/javascript" src="../js/calendar-zh_TW.js"></script>
<script type="text/javascript" src="../js/DateExpFunction.js"></script>
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/jquery.blockUI.js"></script>
<script>

function doDownload(formAction) {
    var alertMessage = "是否確定下載?";
    if (confirm(alertMessage)) {

        var form = document.main;
        //form.action = "ImInventoryCountCompare.jsp";
        //form.target = "action";
        //form.submit();
        msgDiv = document.getElementById("message");
        msgDiv.innerHTML = "庫別：TICO資料下載";
        $.blockUI({
            message: $('#domMessage'),
            overlayCSS: { // 遮罩的css設定
                backgroundColor: '#eee'
            },
            css: { // 遮罩訊息的css設定
                border : '3px solid #aaa',
                width: '30%',
                left : '35%',
                backgroundColor : 'white',
                opacity : '0.9' //透明度，值在0~1之間
            }
        });
        $.ajax({
            url: "ImItemCopyResult.jsp",
            dataType: "html",
            data: $(form).serialize(),
            type: "POST",
            aynsc: false,
            success: function (data) {
                if (data.indexOf('complete') > -1) alert('複製完成！');
                else alert(data.substring(data.indexOf('error:') + 6));
                $.unblockUI();
            }, error: function (data, status, e) {
                alert("Error:" + data);
                $.unblockUI();
            }
        });
    }
}

</script>
<style type="text/css">
</style>
</head>
<body>
<form accept-charset='UTF-8' id='main' name='main' method='POST'>
  <input id='brandCode' name='brandCode' type='HIDDEN' value='<%=brandCode %>'>
  <input name='pageStyle' type='HIDDEN' value='no_popup'>
  <table align="center" class=MsoNormalTable border=0 cellspacing=1 cellpadding=0 width="100%" style='width:100.0%;mso-cellspacing:.7pt;background:white;mso-padding-alt: 1.5pt 1.5pt 1.5pt 1.5pt'>
    <tr style='mso-yfti-irow:0;mso-yfti-firstrow:yes'>
      <td colspan=9 style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; background:#990000;padding:1.5pt 1.5pt 1.5pt 1.5pt'><p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'> <b><span style='font-size:10.5pt;mso-bidi-font-size:12.0pt;font-family:新細明體;mso-ascii-font-family:Arial;mso-hansi-font-family:Arial;mso-bidi-font-family:Arial;color:white;mso-font-kerning:0pt'>商品資料轉拋作業(T1CO->T6CO)</span></b> </p></td>
    </tr>
    <tr style='mso-yfti-irow:1;mso-yfti-firstrow:yes'>
      <td align='middle' style='border:solid #990000 1.0pt;mso-border-alt:solid #990000 .75pt; padding:1.5pt 1.5pt 1.5pt 1.5pt'>
        <p class=MsoNormal style='line-height:18.0pt;mso-pagination:widow-orphan'>
          <input name="download" type='button' value='商品資料複製' onclick='doDownload("callStoreProceduce")'>
        </p>
      </td>
    </tr>
  </table>
  <%
	}catch(Exception ex){
		ex.printStackTrace();
	}
%>
</form>
<div id="domMessage" style="display:none;">
    <table>
    	<tr>
      		<td align="center" valign="middle">
        		<font size=4><b>資料複製中，請稍後...</b></font>
        		<div id="message"></div>
      		</td>
    	</tr>
    </table>
</div>
<form name=form2>
  <iframe id=action name=action width=0 height=0 TITLE="ACTIONFRAME" scrolling="auto" src="blank.html" frameborder=0></iframe>
</form>
</body>
</html>
