<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");
%>
<html>
<head>
  <title>Группы</title>
</head>

<body>



<table>
  <tr>
    <th>Фамилия</th>
    <th>Имя</th>
    <th>Отчество</th>
    <th>Группа</th>
    <th>Возраст</th>
    <th>Пол</th>
    <th>email</th>
    <th>Родной город</th>
  </tr>
  <%
    for (String ss[] : list) {

  %>
  <tr>
    <td align="center"><%=ss[0]%></td>
    <td align="center"><%=ss[1]%></td>
    <td align="center"><%=ss[2]%></td>
    <td align="center"><%=ss[3]%></td>
    <td align="center"><%=ss[4]%></td>
    <td align="center"><%=ss[5]%></td>
    <td align="center"><%=ss[6]%></td>
    <td align="center"><%=ss[7]%></td>
  </tr>
  <% }%>
</table>



<br>
<br>
<a href="/main">Назад к главной странице</a>

</body>

</html>
