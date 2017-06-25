<%@ page import="java.util.ArrayList" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ArrayList<String[]> list = (ArrayList<String[]>) request.getAttribute("list");
  Boolean isRec = (Boolean) request.getAttribute("isRec");
%>
<html>
<head>
  <title>Факультеты</title>
</head>

<body>


<%
  if (!isRec) {
%>

<%
  for (String ss[] : list) {

%>

<form name="submitForm<%=ss[0]%>" action="/departments" method="POST">
  <input type="hidden" name="id" value="<%=ss[0]%>">
</form>

<%}%>

<table>
  <tr>
    <th>Наименование</th>
    <th>Количество групп</th>
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
    <td align="center"><%=ss[4]%></td>
  </tr>
<% }%>
</table>

<% } else { %>

<%
  for (String ss[] : list) {

%>

<form name="submitForm<%=ss[2]%>" action="/groups" method="POST">
  <input type="hidden" name="id" value="<%=ss[2]%>">
</form>

<%}%>


<table>
  <tr>
    <th>Наименование факультета</th>
    <th>Наименование группы</th>
  </tr>


  <%
    for (String ss[] : list) {
      String href = "javascript:document.submitForm"+ss[2]+".submit()";
  %>
  <tr>
    <td align="center"><%=ss[0]%></td>
    <td align="center"><a href=<%=href%>><%=ss[1]%></a></td>
  </tr>
  <% }%>
</table>


<%}%>

<br>
<br>
<a href="/main">Назад к главной странице</a>

</body>

</html>
