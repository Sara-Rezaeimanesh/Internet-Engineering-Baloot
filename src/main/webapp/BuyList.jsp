<%@ page import="all.domain.Amazon.Amazon" %>
<%@ page import="all.domain.User.User" %>

<%
    Amazon amazon = Amazon.getInstance();
    String activeUser = amazon.getActiveUser();
    User user = null;
    try {
        user = Amazon.getInstance().findUserById(activeUser);
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
%>

<html lang="en"><head>
    <meta charset="UTF-8">
    <title>User</title>
    <style>
        li {
        	padding: 5px
        }
        table{
            width: 100%;
            text-align: center;
        }
    </style>
</head>
<body>
    <ul>
        <%= user.createHTMLForUser() %>
    </ul>
    <table>
        <caption>
            <h2>Buy List</h2>
        </caption>
        <tbody>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Provider Name</th>
            <th>Price</th>
            <th>Categories</th>
            <th>Rating</th>
            <th>In Stock</th>
            <th></th>
            <th></th>
        </tr>
            <%= user.createHTMLForBuyList("remove") %>
        <tr>
            <th>
            <form action="/baloot/discount" method="post">
                <input type="text" name="discount" />
                <button type="submit">apply discount</button>
            </form>
            </th>
        </tr>
        </tbody>
    </table>

</body></html>