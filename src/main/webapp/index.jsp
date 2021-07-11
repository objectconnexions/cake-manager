<%@ page import = "java.io.*,java.util.*" %>

<html>
   <head>
      <title>Cake Manager</title>
   </head>
   
   <body>
      <center>
         <h1>Code Update</h1>
      </center>
      <%
         String site = new String("/ui/list/cake");
         response.setStatus(response.SC_MOVED_TEMPORARILY);
         response.setHeader("Location", site); 
      %>
   </body>
</html>