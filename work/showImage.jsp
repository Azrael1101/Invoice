<%@ page language="java" import="org.hibernate.lob.SerializableBlob,java.sql.Blob,java.util.*,java.io.*,javax.servlet.ServletOutputStream,tw.com.tm.erp.hbm.bean.ImItemImage,tw.com.tm.erp.hbm.dao.DAOFactory,tw.com.tm.erp.hbm.dao.ImItemImageDAO" pageEncoding="BIG5"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<%
ImItemImageDAO imidao = DAOFactory.getInstance().getImItemImageDAO();
java.lang.Long imageId=new java.lang.Long(0L);
if(request.getParameter("imageId")!=null)
{
	imageId=new java.lang.Long(request.getParameter("imageId"));
}
ImItemImage item=imidao.findById(imageId);

if (request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
    // MS IE5.5 有要作特別處理
    response.setHeader("Content-Disposition","filename="
            + new String( item.getImageName().getBytes("Big5"), "ISO8859_1" ) );
  }
  else {
    // 非 IE5.5 的 Header 設定方式
    response.addHeader( "Content-Disposition", "attachment;filename="
          + new String( item.getImageName().getBytes("Big5"), "ISO8859_1" ) );
  }

%>
<meta http-equiv="Content-Type" content="<%=item.getContentType()%>" />
<%if(item.getContent()!=null){
/**SerializableBlob sBlob = (SerializableBlob)item.getContent();
Blob blob = (Blob)sBlob.getWrappedBlob();
InputStream inputStream = blob.getBinaryStream();*/
InputStream inputStream = item.getContent().getBinaryStream();
	OutputStream outStream;
outStream=null;
 outStream= response.getOutputStream();
 
        response.setContentType(item.getContentType()); 

byte[] buf = new byte[1];
int len = 0;
while((len = inputStream.read(buf)) != -1) { 
        outStream.write(buf, 0, len);        
}
outStream.flush();
if(outStream!=null){outStream.close();}
inputStream.close();
out.clear();
out = pageContext.pushBody();
response.setStatus(response.SC_OK );
response.flushBuffer();
}
%>

 </head>

<BODY>

</body>
</html>
