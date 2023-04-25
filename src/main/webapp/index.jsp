<%@ page import="com.baloot.IE.domain.Amazon.Amazon" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Home</title>
</head>

<%
    String activeUser = Amazon.getInstance().getActiveUser();
%>

<body>
<ul>
    <li id="email">username: <%=activeUser%></li>
    <li>
        <a href="${pageContext.request.contextPath}/commodities">Commodities</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/buyList">Buy List</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/credit">Add Credit</a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}/logout">Log Out</a>
    </li>
</ul>

</body></html>