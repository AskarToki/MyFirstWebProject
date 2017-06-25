<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");
  Boolean isRec = (Boolean) request.getAttribute("isRec");
%>
<html>
<head>
  <title>Группы</title>
</head>

<body>


<%
  if (!isRec) {
%>

<%
  for (String ss[] : list) {

%>

<form name="submitForm<%=ss[0]%>" action="/groups" method="POST">
  <input type="hidden" name="id" value="<%=ss[0]%>">
</form>

<%}%>

<table>
  <tr>
    <th>Наименование группы</th>
    <th>Количество студентов</th>
    <th>Количество предметов</th>
  </tr>
  <%
    for (String ss[] : list) {
      String href = "javascript:document.submitForm"+ss[0]+".submit()";
  %>
  <tr>
    <td align="center"><a href=<%=href%>><%=ss[1]%></a></td>
    <td align="center"><%=ss[2]%></td>
    <td align="center"><%=ss[3]%></td>
  </tr>
  <% }%>
</table>

<% } else { %>

<table>
  <tr>
    <th>Фамилия</th>
    <th>Имя</th>
    <th>Отчество</th>
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
  </tr>
  <% }%>
</table>


<%}%>

<br>
<br>
<a href="/main">Назад к главной странице</a>

</body>

</html>
