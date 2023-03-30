<%@ page import="all.domain.Amazon.Amazon" %>
<%--
  Created by IntelliJ IDEA.
  User: N.GH
  Date: 3/29/2023
  Time: 2:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    String errorMsg = Amazon.getErrorMsg();
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Error</title>
    </head>
    <body>
        <a href="/baloot/">Home</a>
        <h1><%=errorMsg%></h1>
    </body>
</html>
