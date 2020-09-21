<jsp:include page="header.jsp"/>
<table class="table">
     <tr>
             <th>Id</th>
             <td>${user.userId}</td>
             </tr>
</table>
<form action="/blockchain-web/${user.userId}/user-cabinet/update" method="post">
  <div class="form-group">
        <label for="formGroupExampleInput2">Name</label>
        <input type="text" class="form-control" name="userName" value="${user.userName}" id="formGroupExampleInput1" placeholder="Name">
  </div>
<div class="form-group">
        <label for="formGroupExampleInput2">Email</label>
        <input type="text" class="form-control" name="email" value="${user.email}" id="formGroupExampleInput1" placeholder="Email">
  </div>
  <div class="form-group">
          <label for="formGroupExampleInput2">Phone</label>
          <input type="text" class="form-control" name="mobile" value="${user.mobile}" id="formGroupExampleInput1" placeholder="Phone">
    </div>
    <button type="submit" class="btn btn-primary">Submit</button>
    </form>

<jsp:include page="footer.jsp"/>