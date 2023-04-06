<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Commodities</title>
    <style>
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>

<%@ page import="all.domain.Amazon.Amazon" %>

<%
    Amazon amazon = Amazon.getInstance();
    String activeUser = amazon.getActiveUser();
%>
<body>
    <a href="${pageContext.request.contextPath}/">Home</a>
    <p id="username">username: <%=activeUser%></p>
    <br><br>
    <form action="${pageContext.request.contextPath}/filterProducts" method="POST">
        <label>Search:</label>
        <input type="text" name="search" value="">
        <button type="submit" name="action" value="search_by_category">Search By Cagtegory</button>
        <button type="submit" name="action" value="search_by_name">Search By Name</button>
        <button type="submit" name="action" value="search_by_id">Search By Id</button>
        <button type="submit" name="action" value="clear">Clear Search</button>
    </form>
    <br><br>
    <form action="${pageContext.request.contextPath}/filterProducts" method="POST">
        <label>Sort By:</label>
        <button type="submit" name="action" value="sort_by_rate">Rate</button>
    </form>
    <form action="${pageContext.request.contextPath}/filterProducts" method="POST">
        <label>Sort By:</label>
        <button type="submit" name="action" value="sort_by_price">Price</button>
    </form>
    <br><br>
    <table>
        <tr>
            <th>Id</th>
            <th>Name</th> 
            <th>Provider Name</th>
            <th>Price</th>
            <th>Categories</th>
            <th>Rating</th>
            <th>In Stock</th>
            <th>Links</th>
        </tr>
        <%=amazon.convertProductsToListItems()%>
    </table>
</body>
</html>