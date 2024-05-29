<%@ page language="java" import="java.util.*,java.io.*,java.net.*" pageEncoding="BIG5"%>

<%
String name = new String(request.getParameter("fileName").getBytes( "ISO-8859-1"),"UTF-8");
String path = new String(request.getParameter("filePath").getBytes( "ISO-8859-1"),"UTF-8");
String fileName = new String(request.getParameter("name").getBytes( "ISO-8859-1"),"UTF-8");
if(name==null||path==null||fileName==null){
%>
<script type="text/javascript">
alert("檔案開啟錯誤!");
window.close();
</script>
<% 
}else{

  File file = new File(path+name);
  if(file.exists()){//檢驗檔案是否存在
        try{
            response.setHeader("Content-Disposition","attachment; filename=\""  + URLEncoder.encode(fileName, "UTF-8") + "\"");  
            OutputStream output = response.getOutputStream();
            InputStream in = new FileInputStream(file);
            byte[] b = new byte[2048];
            int len;
           
            while((len = in.read(b))>0){
              output.write(b,0,len);
            }
            in.close();
            output.flush();
            output.close();   //關閉串流
            out.clear();
            out = pageContext.pushBody();
        }catch(Exception ex){
            %>
	    <script type="text/javascript">
		alert("無法開啟檔案");
		</script>
        
        <%
        }
    }else{%>
	    <script type="text/javascript">
		alert("此檔案不存在");
		</script>
        
        <%
    }

}


%>
 <script type="text/javascript">
		window.close();
		</script>

