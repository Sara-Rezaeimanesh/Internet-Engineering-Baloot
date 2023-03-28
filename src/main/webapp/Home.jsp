<%@ page import="all.domain.Amazon.Amazon" %>
<html lang="en"><head>
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
            <a href="/commodities">Commodities</a>
        </li>
        <li>
            <a href="/buyList">Buy List</a>
        </li>
        <li>
            <a href="/credit">Add Credit</a>
        </li>
        <li>
            <a href="/logout">Log Out</a>
        </li>
    </ul>

</body></html>