<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="header.jsp"/>
    <h1>Recipients:</h1>

    <table class="table">
      <thead>
        <tr>
          <th scope="col">#</th>
          <th scope="col">User ID</th>
          <th scope="col">Email</th>
          <th scope="col">Mobile Phone</th>
          <th scope="col">Image</th>
        </tr>
      </thead>
      <tbody>
<c:forEach items="${users}" var="user">
        <tr>
          <th scope="row">${user.id}</th>
          <td>${user.userId}</td>
          <td>${user.emailAddress}</td>
          <td>${user.mobilePhone}</td>
          <td><img src="/hello-spring/${user.id}/image" width=48 height=64 alt="recipient_${user.id}_no_image"></td>
          <td><p><a href="/hello-spring/${user.id}/recipient.html" class="text-primary">Edit</a></p><td>
        </tr>
</c:forEach>
      </tbody>

    </table>
<jsp:include page="footer.jsp"/>