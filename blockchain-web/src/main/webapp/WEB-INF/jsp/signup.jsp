<jsp:include page="header.jsp"/>
<h1>Sign Up: </h1>
<form method="post" enctype="multipart/form-data">
   <div class="form-group">
      <label for="exampleFormControlFile1">Select image:</label>
      <input type="file" class="form-control-file" name="image" id="exampleFormControlFile1">
   </div>
<form action="/blockchain-web/signup" method="post">
   <div class="form-group">
      <label for="formGroupExampleInput2">Name</label>
      <input type="text" class="form-control" name="userName" id="formGroupExampleInput1" placeholder="Name">
   </div>
   <div class="form-group">
      <label for="exampleInputEmail1">Email address</label>
      <input type="email" class="form-control" name="email" id="exampleInputEmail1" aria-describedby="emailHelp" placeholder="Enter email">
      <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
   </div>
   <div class="form-group">
      <label for="formGroupExampleInput2">Mobile phone</label>
      <input type="text" class="form-control" name="mobile" id="formGroupExampleInput2" placeholder="Mobile phone">
   </div>
   <div class="form-group">
      <label for="formGroupExampleInput2">Password</label>
      <input type="password" class="form-control" name="userPassword" id="formGroupExampleInput2" placeholder="Password">
   </div>
   <div class="form-group">
      <label for="formGroupExampleInput2">Confirm password</label>
      <input type="password" class="form-control" name="confirmPassword" id="formGroupExampleInput2" placeholder="Confirm password">
   </div>
   <button type="submit" class="btn btn-primary">Submit</button>
</form>
<jsp:include page="footer.jsp"/>