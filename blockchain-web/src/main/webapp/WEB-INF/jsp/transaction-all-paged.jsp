<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Spring MVC pagination tutorial</title>
</head>
<body>
<c:set value="${userList}" var="userPageList" />
<table>
<tr>
<th>Id</th>
<th>Value</th>
</tr>
<c:forEach items="${userPageList.pageList}" var="user">
<tr>
<td>${user.transactionId}</td>
<td>${user.value}</td>
</tr>
</c:forEach>
</table>
<br/>
<c:choose>
  <%-- Show Prev as link if not on first page --%>
  <c:when test="${userPageList.firstPage}">
    <span>Prev</span>
  </c:when>
  <c:otherwise>
    <c:url value="/all-transactions/prev" var="url" />
    <a href='<c:out value="${url}" />'>Prev</a>
  </c:otherwise>
</c:choose>
<c:forEach begin="1" end="${userPageList.pageCount}" step="1"  varStatus="tagStatus">
  <c:choose>
    <%-- In PagedListHolder page count starts from 0 --%>
    <c:when test="${(userPageList.page + 1) == tagStatus.index}">
      <span>${tagStatus.index}</span>
    </c:when>
    <c:otherwise>
      <c:url value="/all-transactions/${tagStatus.index}" var="url" />
      <a href='<c:out value="${url}" />'>${tagStatus.index}</a>
    </c:otherwise>
  </c:choose>
</c:forEach>
<c:choose>
  <%-- Show Next as link if not on last page --%>
  <c:when test="${userPageList.lastPage}">
    <span>Next</span>
  </c:when>
  <c:otherwise>
    <c:url value="/all-transactions/next" var="url" />
    <a href='<c:out value="${url}" />'>Next</a>
  </c:otherwise>
</c:choose>
</body>
</html>