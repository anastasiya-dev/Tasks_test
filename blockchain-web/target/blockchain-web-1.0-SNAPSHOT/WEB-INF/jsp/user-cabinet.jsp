<jsp:include page="header.jsp"/>
<table class="table">

        <tr>
        <th>Id</th>
        <td>${user.userId}</td>
        </tr>
        <tr>
        <th>Name</th>
          <td>${user.userName}</td>
         </tr>
         <tr>
         <th>Email</th>
          <td>${user.email}</td>
          </tr>
          <tr>
          <th>Phone</th>
          <td>${user.mobile}</td>
          </tr>
      </table>
<a href="/blockchain-web/${userId}/user-cabinet/edit" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Edit</a>
<jsp:include page="footer.jsp"/>