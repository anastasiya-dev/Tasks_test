<jsp:include page="header.jsp"/>
    <h1>Recipient:</h1>

<form action="/hello-spring/recipient.html" method="post" enctype="multipart/form-data">
  <div class="form-group">
      <label for="exampleFormControlFile1">Select image:</label>
      <input type="file" class="form-control-file" name="image" id="exampleFormControlFile1">
  </div>
  <div class="form-group">
        <label for="formGroupExampleInput2">User name</label>
        <input type="text" class="form-control" name="userId" value="${user.userId}" id="formGroupExampleInput1" placeholder="User name">
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">Email address</label>
    <input type="email" class="form-control" name="emailAddress" value="${user.emailAddress}" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
    <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
  </div>
  <div class="form-group">
      <label for="formGroupExampleInput2">Mobile phone</label>
      <input type="text" class="form-control" name="mobilePhone" value="${user.mobilePhone}" id="formGroupExampleInput2" placeholder="Mobile phone">
  </div>
  <input type="hidden" name="id" value="${user.id}"/>
  <button type="submit" class="btn btn-primary">Submit</button>
</form>

<jsp:include page="footer.jsp"/>