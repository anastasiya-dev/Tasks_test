     <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

     <jsp:include page="header.jsp"/>
    <h1>Search result:</h1>

    <ul class="list-group">
      <c:forEach items="${users}" var="user">
      <li class="list-group-item"><c:out value="${user}"/>
      <img src="/hello-spring/${fn:substring(user, 0, 36)}/image" width=48 height=64 alt="recipient_${fn:substring(user, 0, 36)}_no_image">
      </li>
      </c:forEach>
    </ul>

    <jsp:include page="footer.jsp"/>