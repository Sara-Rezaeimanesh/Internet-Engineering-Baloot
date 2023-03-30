<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <title>Commodity</title>
    <style>
      li {
        padding: 5px;
      }
      table {
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
    <span>username: <%=activeUser%></span>
    <br>
    <ul>
      <%=amazon.getChosenProductHTML()%>
    </ul>

    <label>Add Your Comment:</label>
    <form action="${pageContext.request.contextPath}/addComment" method="post">
      <input type="text" name="comment" value="" />
      <button type="submit">submit</button>
    </form>
    <br>
    <form action="${pageContext.request.contextPath}/rateCommodity" method="POST">
      <label>Rate(between 1 and 10):</label>
      <input type="number" id="quantity" name="quantity" min="1" max="10">
      <button type="submit">Rate</button>
    </form>
    <br>
    <form action="${pageContext.request.contextPath}/addToBuyList" method="POST">
      <button type="submit">Add to BuyList</button>
    </form>
    <br />
    <table>
      <caption><h2>Comments</h2></caption>
      <tr>
        <th>username</th>
        <th>comment</th>
        <th>date</th>
        <th>likes</th>
        <th>dislikes</th>
      </tr>
      <%=amazon.getProuctComments()%>
    </table>
    <br><br>
    <table>
      <caption><h2>Suggested Commodities</h2></caption>
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
        <tr>
            <td>2341</td>
            <td>Galaxy S22</td> 
            <td>Phone Provider No.1</td>
            <td>34000000</td>
            <td>Technology, Phone</td>
            <td>8.3</td>
            <td>17</td>
            <td><a href="/commodities/2341">Link</a></td>
        </tr>
        <tr>
            <td>4231</td>
            <td>Galaxy S22 Plus</td> 
            <td>Phone Provider No.1</td>
            <td>43000000</td>
            <td>Technology, Phone</td>
            <td>8.7</td>
            <td>12</td>
            <td><a href="/commodities/4231">Link</a></td>
        </tr>
        <tr>
          <td>1234</td>
          <td>Galaxy S22 Ultra</td> 
          <td>Phone Provider No.2</td>
          <td>50000000</td>
          <td>Technology, Phone</td>
          <td>8.9</td>
          <td>5</td>
          <td><a href="/commodities/1234">Link</a></td>
      </tr>
      <tr>
          <td>4321</td>
          <td>Galaxy A53</td> 
          <td>Phone Provider No.2</td>
          <td>16000000</td>
          <td>Technology, Phone</td>
          <td>8.7</td>
          <td>11</td>
          <td><a href="/commodities/4321">Link</a></td>
      </tr>
    </table>
  </body>
</html>
