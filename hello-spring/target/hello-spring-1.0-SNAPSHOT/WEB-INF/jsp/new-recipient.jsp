<jsp:include page="header.jsp"/>
<h1>New recipient: </h1>

<form action="/hello-spring/new-recipient.html" method="post">
  <div class="form-group">
        <label for="formGroupExampleInput2">User name</label>
        <input type="text" class="form-control" name="userId" id="formGroupExampleInput1" placeholder="User name">
  </div>
  <div class="form-group">
    <label for="exampleInputEmail1">Email address</label>
    <input type="email" class="form-control" name="emailAddress" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
    <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
  </div>
  <div class="form-group">
      <label for="formGroupExampleInput2">Mobile phone</label>
      <input type="text" class="form-control" name="mobilePhone" id="formGroupExampleInput2" placeholder="Mobile phone">
  </div>
  <button type="submit" class="btn btn-primary">Submit</button>
</form>

<jsp:include page="footer.jsp"/>