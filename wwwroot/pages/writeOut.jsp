<%@ page language="java" import="java.io.*, java.sql.*, java.util.*" %>
<%
   // This JSP will save the request Input Steam into a file.
   String fileOut = "C:/Temp/writeOut.bin." + java.util.Calendar.getInstance().getTime().getTime();
    try{
      ServletInputStream in = request.getInputStream();
      byte[] line = new byte[1024];
      int bytes = 0;

      FileOutputStream fileOutS = new  FileOutputStream(fileOut);

      while(0 <(bytes = in.read(line))){
        fileOutS.write(line,0, bytes);
      }

      fileOutS.close();
      fileOutS = null;
      out.println("Upload Stream Saved to \"" + fileOut + "\".");
      out.println("SUCCESS");
    }catch(Exception e){
      out.println("ERROR : Exception \"" + e.getMessage() + "\" Occured.");
    }
%>