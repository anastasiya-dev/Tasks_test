<jsp:include page="header.jsp"/>
<table class="table" style="margin-top: 4cm;">

        <tr>
        <th style="width: 10em">Id</th>
        <td style="text-align:left; width: 25em">${user.userId}</td>

        </tr>
        <tr>
        <th style="width: 10em">Name</th>
          <td style="text-align:left; width: 25em">${user.userName}</td>

         </tr>
         <tr style="width: 10em">
         <th>Email</th>
          <td style="text-align:left; width: 25em">${user.email}</td>

          </tr>
          <tr style="width: 10em">
          <th>Phone</th>
          <td style="text-align:left; width: 25em">${user.mobile}</td>

          </tr>
      </table>
<a href="/blockchain-web/${userId}/user-cabinet/edit" class="btn btn-primary btn-lg active" role="button" aria-pressed="true">Edit</a>
<jsp:include page="footer.jsp"/>